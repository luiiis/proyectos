# Módulo 27: CI/CD con GitHub Actions

## 1. Pipeline Completo para Angular

```yaml
# .github/workflows/ci.yml
name: CI/CD Angular

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 20
          cache: 'npm'
      - run: npm ci
      - run: npm run lint

  test:
    runs-on: ubuntu-latest
    needs: lint
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 20
          cache: 'npm'
      - run: npm ci
      - run: npm run test -- --no-watch --code-coverage
      - uses: actions/upload-artifact@v4
        with:
          name: coverage
          path: coverage/

  build:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 20
          cache: 'npm'
      - run: npm ci
      - run: npm run build -- --configuration=production
      - uses: actions/upload-artifact@v4
        with:
          name: dist
          path: dist/

  deploy:
    runs-on: ubuntu-latest
    needs: build
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/download-artifact@v4
        with:
          name: dist
          path: dist/
      - name: Deploy to S3
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        run: |
          aws s3 sync dist/ s3://${{ vars.S3_BUCKET }}/ --delete
          aws cloudfront create-invalidation \
            --distribution-id ${{ vars.CF_DISTRIBUTION_ID }} \
            --paths "/*"
```

## 2. Flujo del Pipeline

```
┌────────┐    ┌────────┐    ┌────────┐    ┌────────┐
│  LINT  │───>│  TEST  │───>│ BUILD  │───>│ DEPLOY │
│        │    │        │    │        │    │        │
│ ESLint │    │ Karma  │    │ ng     │    │ S3 +   │
│ Prettier│   │ Coverage│   │ build  │    │ CDN    │
└────────┘    └────────┘    └────────┘    └────────┘
                                          (solo main)
```

## 3. Cache y Optimización

```yaml
# Cache de node_modules (reduce tiempo de CI)
- uses: actions/setup-node@v4
  with:
    node-version: 20
    cache: 'npm'  # cachea basado en package-lock.json

# Cache de Angular build
- uses: actions/cache@v4
  with:
    path: .angular/cache
    key: angular-${{ hashFiles('package-lock.json') }}
```

## 4. Deploy con Docker

```yaml
  deploy-docker:
    runs-on: ubuntu-latest
    needs: [test]
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v4
      - name: Login to Container Registry
        run: echo "${{ secrets.REGISTRY_TOKEN }}" | docker login ghcr.io -u ${{ github.actor }} --password-stdin
      - name: Build and Push
        run: |
          docker build -t ghcr.io/${{ github.repository }}/frontend:${{ github.sha }} .
          docker build -t ghcr.io/${{ github.repository }}/frontend:latest .
          docker push ghcr.io/${{ github.repository }}/frontend:${{ github.sha }}
          docker push ghcr.io/${{ github.repository }}/frontend:latest
```

## 5. PR Checks

```yaml
# Solo en Pull Requests: comentar coverage y bundle size
  pr-report:
    runs-on: ubuntu-latest
    if: github.event_name == 'pull_request'
    steps:
      - run: npm run build -- --stats-json
      - name: Bundle Size Report
        uses: andresz1/size-limit-action@v1
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
```

## 6. Ejercicios

1. Crea un workflow de GitHub Actions con lint, test y build para tu proyecto Angular.
2. Agrega deploy automático a S3/CloudFront cuando se hace push a main.
3. Configura cache de npm y Angular para reducir tiempos de CI.
4. Implementa un job que publique la imagen Docker en un registry.
5. Agrega un check de bundle size en Pull Requests.

---

## Siguiente Módulo
→ [28-Arquitectura](../28-arquitectura/README.md)
