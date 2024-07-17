import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../model/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8082/produto';

  constructor(private http: HttpClient) {}

  addProduct(request: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, request);
  }

  updateProduct(productId: string, request: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}`, request);
  }

  deleteProduct(productId: string): Observable<Product> {
    return this.http.delete<Product>(`${this.apiUrl}/${productId}`);
  }

  getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(this.apiUrl + '/' + id);
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  upload(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post(this.apiUrl + '/upload', formData, {
      reportProgress: true,
      observe: 'events',
    });
  }
}
