# Regras de Tempo do Simulador

## Duração de Consulta por Nível de Urgência

O tempo de atendimento varia conforme o nível de urgência do utente:

| Nível de Urgência | Duração da Consulta | Unidades de Tempo |
|-------------------|---------------------|-------------------|
| **VERDE** (Baixa) | 1 hora | 1 unidade |
| **LARANJA** (Média) | 2 horas | 2 unidades |
| **VERMELHA** (Urgente) | 3 horas | 3 unidades |

### Exemplo:
```
Utente com urgência VERDE → Consulta dura 1 unidade
Utente com urgência LARANJA → Consulta dura 2 unidades
Utente com urgência VERMELHA → Consulta dura 3 unidades
```

## Descanso de Médicos

Os médicos precisam de períodos de descanso após trabalharem consecutivamente:

### Regra:
**1 unidade de descanso por cada 5 unidades trabalhadas consecutivamente**

### Como Funciona:
1. O sistema rastreia as horas trabalhadas consecutivas de cada médico
2. Quando um médico completa **5 ou mais unidades** de trabalho consecutivo, entra em descanso obrigatório
3. O médico fica **indisponível** durante o período de descanso
4. Após o descanso, o contador de horas consecutivas é **reiniciado**

### Cálculo do Descanso:
```
Horas de descanso = floor(Horas trabalhadas consecutivas / 5)

Exemplos:
- 5 horas trabalhadas → 1 unidade de descanso
- 7 horas trabalhadas → 1 unidade de descanso
- 10 horas trabalhadas → 2 unidades de descanso
- 15 horas trabalhadas → 3 unidades de descanso
```

### Notificações:
```
💤 NOTIFICAÇÃO: Dr(a). João Martins precisa de 1 unidade(s) de descanso 
(trabalhou 5 horas consecutivas).

✅ NOTIFICAÇÃO: Dr(a). João Martins terminou descanso e está DISPONÍVEL.
```

## Escalamento de Urgência

Os utentes que aguardam muito tempo têm sua urgência escalada automaticamente:

### Regras de Escalamento:

| Transição | Tempo de Espera | Ação |
|-----------|----------------|------|
| **VERDE → LARANJA** | 3 unidades | Urgência aumenta para LARANJA |
| **LARANJA → VERMELHA** | 3 unidades | Urgência aumenta para VERMELHA |
| **VERMELHA → Saída** | 2 unidades | Utente deve sair da urgência (crítico) |

### Detalhes:

#### 1. VERDE para LARANJA (3 unidades)
- Utente com urgência VERDE aguarda 3 ou mais unidades
- Sistema escalona automaticamente para LARANJA
- Relógio de espera é **reiniciado**

```
⚠️  NOTIFICAÇÃO: Utente Maria escalou de VERDE para LARANJA 
(tempo de espera: 3 unidades)
```

#### 2. LARANJA para VERMELHA (3 unidades)
- Utente com urgência LARANJA aguarda 3 ou mais unidades
- Sistema escalona automaticamente para VERMELHA
- Relógio de espera é **reiniciado**

```
🚨 NOTIFICAÇÃO: Utente Pedro escalou de LARANJA para VERMELHA 
(tempo de espera: 3 unidades)
```

#### 3. VERMELHA para Saída de Urgência (2 unidades)
- Utente com urgência VERMELHA aguarda 2 ou mais unidades
- **SITUAÇÃO CRÍTICA**: Utente deve ser transferido/removido
- Sistema **remove automaticamente** o utente da fila

```
🚨 CRÍTICO: Utente Ana deve sair da urgência! 
(tempo de espera crítico: 2 unidades)
```

## Fluxo Completo - Exemplo Prático

### Cenário: Dr. João Martins atende 3 utentes

```
HORA 8:00
✅ Dr(a). João Martins (Cardiologia) ficou DISPONÍVEL.

HORA 8:00 - Utente Maria (VERDE) inicia atendimento
Duração prevista: 1 unidade

HORA 9:00
✅ Utente Maria terminou atendimento com Dr(a). João Martins (1 unidade)
Horas trabalhadas consecutivas: 1

HORA 9:00 - Utente Pedro (LARANJA) inicia atendimento
Duração prevista: 2 unidades

HORA 11:00
✅ Utente Pedro terminou atendimento com Dr(a). João Martins (2 unidades)
Horas trabalhadas consecutivas: 3

HORA 11:00 - Utente Ana (VERMELHA) inicia atendimento
Duração prevista: 3 unidades

HORA 14:00
✅ Utente Ana terminou atendimento com Dr(a). João Martins (3 unidades)
Horas trabalhadas consecutivas: 6
💤 Dr(a). João Martins precisa de 1 unidade(s) de descanso 
(trabalhou 6 horas consecutivas).

HORA 15:00
✅ Dr(a). João Martins terminou descanso e está DISPONÍVEL.
Horas trabalhadas consecutivas: 0 (reiniciado)
```

### Cenário: Escalamento de Urgência

```
HORA 10:00 - Utente Carlos (VERDE) entra na fila de espera
Nenhum médico disponível

HORA 13:00 (3 unidades depois)
⚠️  Utente Carlos escalou de VERDE para LARANJA (tempo de espera: 3 unidades)

HORA 16:00 (3 unidades depois)
🚨 Utente Carlos escalou de LARANJA para VERMELHA (tempo de espera: 3 unidades)

HORA 18:00 (2 unidades depois)
🚨 CRÍTICO: Utente Carlos deve sair da urgência! 
(tempo de espera crítico: 2 unidades)
[Utente removido automaticamente do sistema]
```

## Implementação Técnica

### Modelo `medico.java`
```java
private double horasTrabalhadasConsecutivas;
private double horasDescansoNecessarias;
private boolean emDescanso;
```

### BLL - Cálculo de Duração
```java
private double calcularDuracaoConsulta(nivelUrgencia urgencia) {
    switch (urgencia) {
        case VERDE: return 1.0;
        case LARANJA: return 2.0;
        case VERMELHA: return 3.0;
        default: return 2.0;
    }
}
```

### BLL - Gestão de Descanso
```java
// Ao terminar atendimento
m.setHorasTrabalhadasConsecutivas(
    m.getHorasTrabalhadasConsecutivas() + duracaoConsulta
);

if (m.getHorasTrabalhadasConsecutivas() >= 5.0) {
    double horasDescanso = Math.floor(
        m.getHorasTrabalhadasConsecutivas() / 5.0
    );
    m.setHorasDescansoNecessarias(horasDescanso);
    m.setEmDescanso(true);
}
```

### BLL - Escalamento de Urgência
```java
double tempoEspera = horaAtual - u.getHoraTriagem();

if (urgencia == VERDE && tempoEspera >= 3.0) {
    u.setUrgenciaCalculada(LARANJA);
    u.setHoraTriagem(horaAtual); // Reinicia contador
}
else if (urgencia == LARANJA && tempoEspera >= 3.0) {
    u.setUrgenciaCalculada(VERMELHA);
    u.setHoraTriagem(horaAtual); // Reinicia contador
}
else if (urgencia == VERMELHA && tempoEspera >= 2.0) {
    // Remove utente da fila (situação crítica)
    removerUtenteDoArray(filaEspera, u);
}
```

## Resumo das Regras

| Aspecto | Regra | Valor |
|---------|-------|-------|
| Consulta VERDE | Duração | 1 unidade |
| Consulta LARANJA | Duração | 2 unidades |
| Consulta VERMELHA | Duração | 3 unidades |
| Descanso Médico | Após trabalho consecutivo | 1 un. descanso / 5 un. trabalho |
| Escalamento VERDE→LARANJA | Tempo de espera | 3 unidades |
| Escalamento LARANJA→VERMELHA | Tempo de espera | 3 unidades |
| Saída VERMELHA | Tempo de espera crítico | 2 unidades |

## Notas Importantes

1. **Horas Consecutivas**: Só são reiniciadas após período de descanso completo
2. **Escalamento**: O relógio de espera é reiniciado após cada escalamento
3. **Saída Crítica**: Utentes em urgência VERMELHA que aguardam 2+ unidades são removidos automaticamente
4. **Descanso Obrigatório**: Médicos não podem atender durante período de descanso
5. **Prioridade**: Médicos em descanso não aparecem como disponíveis no sistema
