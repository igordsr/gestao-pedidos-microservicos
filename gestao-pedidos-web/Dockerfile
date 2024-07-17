# Use uma imagem base do Node.js
FROM node:latest

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o package.json e o package-lock.json para o diretório de trabalho
COPY package.json package-lock.json ./

# Copia todos os arquivos exceto node_modules e dist
COPY ./ ./

# Exclui a pasta node_modules
RUN rm -rf node_modules

# Exclui a pasta dist se ela existir
RUN rm -rf dist

# Instala as dependências do projeto
RUN npm install

# Expõe a porta 62344 (ou a porta que você usa para ng serve)
EXPOSE 62344

# Comando para iniciar o servidor de desenvolvimento
CMD ["npm", "start"]
