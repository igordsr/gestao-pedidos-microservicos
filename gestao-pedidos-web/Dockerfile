# Use uma imagem base do Node.js
FROM node:latest

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o package.json e o package-lock.json para o diretório de trabalho
COPY package.json package-lock.json ./

# Copia todos os arquivos exceto node_modules e dist
COPY . .

# Exclui a pasta node_modules
RUN rm -rf node_modules

# Exclui a pasta dist se ela existir
RUN rm -rf dist

# Instala as dependências do projeto
RUN npm install

# Executa o build do Angular
RUN npm run build

# Instala o servidor HTTP para servir os arquivos de build
RUN npm install -g http-server

# Expõe a porta 80 para acesso externo
EXPOSE 80

# Define o diretório de trabalho para a pasta onde o build foi gerado
WORKDIR /app/dist/gestao-pedidos.web/browser

# Comando para iniciar o servidor http-server
CMD ["http-server", "-p", "80", "-c-1"]
