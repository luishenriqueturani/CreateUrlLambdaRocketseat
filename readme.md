# Redirect URL

Projeto criado no curso gratuito da Rocketseat sobre Lambda Functions.

## Como usar

### POST
Execute uma requisição POST para:
```
https://9p7zw8yxll.execute-api.us-east-2.amazonaws.com
```

Com o body:
```json
{
  "originalUrl": "https://URL_QUE_SERÁ_ENCURTADA",
  "expirationTime": "CAMPO OPCIONAL, COLOQUE UM TIMESTAMP DA DATA DE EXPIRAÇÂO"
}
```
Deverá retornar um código para usar na url do redirect.


### GET
Execute uma requisição GET para:
```
https://9p7zw8yxll.execute-api.us-east-2.amazonaws.com/{urlCode}
```

Deverá redirecionar para a url informada no campo originalUrl.

## Licença

MIT