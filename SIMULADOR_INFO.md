# Sistema de Simulação - Urgência Hospitalar

## Ciclo de 24 Horas

O simulador opera em ciclos de **24 unidades de tempo** (horas):
- Inicia na **Unidade 1** do **Dia 1**
- Cada avanço incrementa 1 unidade de tempo
- Ao atingir a unidade 25, o sistema **reinicia automaticamente** para a Unidade 1 do próximo dia
- Todos os cálculos são mantidos entre dias (utentes em espera, atendimentos em curso)

## Sistema de Notificações

### ✅ Médicos Disponíveis
Quando um médico entra no horário de trabalho:
```
✅ NOTIFICAÇÃO: Dr(a). [Nome] ([Especialidade]) ficou DISPONÍVEL.
```

### ✅ Fim de Atendimento
Quando um utente termina consulta (após 2 horas):
```
✅ NOTIFICAÇÃO: Utente [Nome] terminou atendimento com Dr(a). [Nome]
```

### ⏰ Médico Fora do Horário
Quando médico continua em atendimento após hora de saída:
```
⏰ NOTIFICAÇÃO: Dr(a). [Nome] está fora do horário mas continua em atendimento com [Utente]
```

### ✅ Saída Após Atendimento
Quando médico sai após terminar atendimento fora do horário:
```
✅ NOTIFICAÇÃO: Dr(a). [Nome] saiu do serviço (terminou atendimento fora do horário).
```

## Escalamento de Urgência

Os utentes que aguardam muito tempo têm sua urgência **escalada automaticamente**:

### ⚠️ VERDE → LARANJA
Após **4 horas** de espera:
```
⚠️  NOTIFICAÇÃO: Utente [Nome] escalou de VERDE para LARANJA (tempo de espera: 4h)
```

### 🚨 LARANJA → VERMELHA
Após **6 horas** de espera:
```
🚨 NOTIFICAÇÃO: Utente [Nome] escalou de LARANJA para VERMELHA (tempo de espera: 6h)
```

## Lógica de Médicos

### Entrada ao Serviço
- Médico fica **disponível** exatamente na hora configurada (`horaEntrada`)
- Só fica disponível se não estiver em atendimento

### Saída do Serviço
- Médico **pode sair após** a hora configurada (`horaSaida`) se estiver em atendimento
- Só sai quando **terminar** o atendimento em curso
- Sistema notifica quando médico está fora do horário mas ainda em serviço

### Duração de Atendimento
- Cada atendimento dura **2 unidades de tempo** (2 horas)
- Médico fica indisponível durante o atendimento
- Após terminar, volta a ficar disponível (se dentro do horário)

## Exemplo de Fluxo

```
DIA 1 - HORA 8:00
✅ NOTIFICAÇÃO: Dr(a). João Martins (Cardiologia) ficou DISPONÍVEL.

[Utente registado e triado com urgência VERDE]
[Encaminhado automaticamente para Dr. João Martins]

DIA 1 - HORA 10:00
✅ NOTIFICAÇÃO: Utente Maria terminou atendimento com Dr(a). João Martins

DIA 1 - HORA 12:00
⚠️  NOTIFICAÇÃO: Utente Pedro escalou de VERDE para LARANJA (tempo de espera: 4h)

DIA 1 - HORA 16:00
⏰ NOTIFICAÇÃO: Dr(a). João Martins está fora do horário mas continua em atendimento

DIA 1 - HORA 18:00
✅ NOTIFICAÇÃO: Utente Ana terminou atendimento com Dr(a). João Martins
✅ NOTIFICAÇÃO: Dr(a). João Martins saiu do serviço (terminou atendimento fora do horário).

[Ao atingir hora 25]
🌅 NOVO DIA - DIA 2
⏰ Reiniciando ciclo de 24 horas...
```

## Funcionalidades Implementadas

- ✅ Ciclo de 24 horas com reinício automático
- ✅ Notificações de entrada/saída de médicos
- ✅ Notificações de fim de atendimento
- ✅ Escalamento automático de urgência por tempo de espera
- ✅ Médicos podem sair após horário se em atendimento
- ✅ Controlo de disponibilidade em tempo real
- ✅ Persistência de dados entre dias
