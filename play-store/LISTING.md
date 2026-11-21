# Ficha da Play Store — ReelsFeedLock

Textos e respostas prontos para copiar no Play Console.

## Detalhes do app

- **Nome do app** (máx. 30): `ReelsFeedLock`
- **Pacote:** `com.jhopn.reelsfeedlock`
- **Categoria:** Produtividade (ou Estilo de vida)
- **Preço:** Gratuito · **Anúncios:** Não
- **Política de privacidade (URL):** https://github.com/Jhopn/ReelsFeedLock/blob/master/PRIVACY.md

## Descrição curta (máx. 80)

```
Bloqueie só os Reels e continue usando o feed, os Stories e o Direct.
```

## Descrição completa (máx. 4000)

```
Quer usar o Instagram sem cair no buraco dos Reels? O ReelsFeedLock bloqueia só a tela de Reels e deixa todo o resto liberado.

COMO FUNCIONA
• Abriu a aba Reels ou um Reel pelo feed? O ReelsFeedLock volta para a tela inicial do celular.
• Feed, Stories, Direct, perfil e Explorar continuam funcionando normalmente.
• Reels enviados por amigos nas conversas continuam abrindo (você pode desligar isso).

VOCÊ NO CONTROLE
• Ligue ou desligue o bloqueio de Reels direto no app, quando quiser.
• Escolha se Reels enviados nas conversas ficam liberados.

PRIVACIDADE
• Não coleta nenhum dado.
• Não tem acesso à internet.
• Gratuito e sem anúncios.
• Código aberto: https://github.com/Jhopn/ReelsFeedLock

USO DO SERVIÇO DE ACESSIBILIDADE
O ReelsFeedLock usa a API de serviço de acessibilidade do Android para saber quando a tela de Reels do Instagram está aberta e, nesse caso, ir para a tela inicial do celular. O serviço só observa o app do Instagram, verifica apenas os identificadores das telas e não lê, guarda nem envia mensagens, senhas ou outros textos. A permissão só é pedida depois do seu consentimento e pode ser desativada a qualquer momento.

O ReelsFeedLock não é afiliado, associado nem endossado pelo Instagram ou pela Meta. Baseado no projeto de código aberto FeedLock.
```

## Declaração de acessibilidade (Play Console → Conteúdo do app → Permissões confidenciais)

**O app é uma ferramenta de acessibilidade?** Não (`isAccessibilityTool` = false).

**Descreva como o app usa a API de acessibilidade:**

```
ReelsFeedLock is a digital wellbeing app that blocks only the Reels screen of the Instagram app, at the user's request. The AccessibilityService is restricted to the package com.instagram.android (android:packageNames). It inspects only view resource IDs (for example, whether the Reels tab is selected or the Reels player is visible) and, when the Reels screen is detected, performs GLOBAL_ACTION_HOME. It does not read, store or transmit any text, messages, passwords or other user content. The app has no INTERNET permission and collects no data. Before the user is sent to the accessibility settings, the app shows a prominent in-app disclosure explaining this use and requires the user to tap "Concordo" (I agree). The user can turn blocking on/off inside the app or disable the service at any time.
```

**Vídeo exigido:** grave um vídeo curto (YouTube, pode ser "não listado") mostrando:
1. Abrir o app e tocar em "Abrir configurações de acessibilidade".
2. O aviso aparecendo e você tocando em "Concordo".
3. Ativar o serviço nas configurações.
4. Abrir o Instagram, tocar na aba Reels e o app voltando para a tela inicial.

## Segurança dos dados (Data safety)

- Coleta dados? **Não**
- Compartilha dados? **Não**
- Dados criptografados em trânsito? Não se aplica (não envia dados)
- Usuário pode pedir exclusão? Não se aplica (nenhum dado é coletado)

## Classificação de conteúdo

Questionário IARC: app utilitário, sem violência, sem conteúdo gerado por usuários, sem compras. Resultado esperado: Livre.

## Público-alvo

Maiores de 13 anos (mesma faixa do Instagram).

## Imagens

| Item | Arquivo | Status |
|---|---|---|
| Ícone 512×512 | `play-store/icon-512.png` | Pronto |
| Gráfico de destaque 1024×500 | `play-store/feature-graphic-1024x500.png` | Pronto |
| Capturas de tela do celular (mín. 2) | — | Tirar no celular: tela do app, aviso de consentimento, opções |

## Checklist de publicação

- [ ] Arquivo `LICENSE` (aguardando resposta em https://github.com/caasion/FeedLock/issues/1)
- [ ] Conta de desenvolvedor Google Play (taxa única de US$ 25)
- [ ] Testar o AAB no celular
- [ ] Capturas de tela
- [ ] Vídeo da declaração de acessibilidade
- [ ] Teste fechado com 12 testadores por 14 dias (exigido para contas pessoais novas)
- [ ] Enviar `app/build/outputs/bundle/release/app-release.aab` e ativar a Assinatura de apps do Google Play
