export interface User {
  id: string;
  nome: string;
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  numero: string;
  telefone: string;
  email: string;
  dataNascimento: string;
  cpf: string;
  password: string;
  role: string;
  enderecoCompleto?: string;
}
