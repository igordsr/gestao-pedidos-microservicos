import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { Pedido } from '../model/pedido.model';

@Injectable({
  providedIn: 'root',
})
export class PedidoService {
  private apiUrl = 'http://localhost:8083/pedido';

  constructor(private http: HttpClient) {}

  register(request: Pedido): Observable<Pedido> {
    return this.http.post<Pedido>(this.apiUrl, request);
  }

  payment(id: string): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.apiUrl}/${id}/efetuar-pagamento`, {});
  }

  deliver(id: string): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.apiUrl}/${id}/entregar`, {});
  }

  getPedidos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}`);
  }

  getAllPedidos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/listar-tudo`);
  }

  update(id: string, request: Pedido): Observable<Pedido> {
    return this.http.patch<Pedido>(`${this.apiUrl}/${id}`, request);
  }

  getUser(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }
}
