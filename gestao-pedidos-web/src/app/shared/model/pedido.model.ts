export interface Pedido {
  identificador: string;
  cliente: string;
  itemList: Item[];
  status?: string;
}

export interface Item {
  produto: string;
  quantidade: number;
}
