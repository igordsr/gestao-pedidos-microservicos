import { Component, OnInit } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MenuComponent } from '../shared/components/menu/menu.component';
import { CommonModule } from '@angular/common';
import { CadastroComponent } from '../cadastro/cadastro.component';
import { User } from '../shared/model/user.model';
import { ProductsComponent } from './components/products/products.component';
import { Auth } from '../auth/service/login.model';
import { LoadingComponent } from '../shared/components/loading/loading.component';
import { ProductsToBuyComponent } from '../products-to-buy/products-to-buy.component';
import { CustomersComponent } from '../customers/customers.component';
import { CartComponent } from './components/cart/cart.component';
import { PedidoComponent } from './components/pedidos/pedidos.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MenuComponent,
    CadastroComponent,
    CadastroComponent,
    ProductsComponent,
    LoadingComponent,
    ProductsToBuyComponent,
    CustomersComponent,
    CartComponent,
    PedidoComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  public perfil = true;
  public product = false;
  public customer = false;
  public cart = false;
  public pedido = false;
  public type: string | null = null;
  public isLoading: boolean = false;
  public reloadCart = false;
  public reloadPedido = false;

  ngOnInit(): void {
    const auth: Auth = JSON.parse(localStorage.getItem('auth') || '');
    this.type = auth.authorities[0].authority.split('_')[1];
  }

  changeTabMenu(tab: any) {
    if (tab === 'product') {
      this.product = true;
      this.perfil = false;
      this.customer = false;
      this.cart = false;
      this.pedido = false;
      this.reloadCart = false;
      this.reloadPedido = false;
    }
    if (tab === 'perfil') {
      this.product = false;
      this.perfil = true;
      this.customer = false;
      this.cart = false;
      this.pedido = false;
      this.reloadCart = false;
      this.reloadPedido = false;
    }
    if (tab === 'customer') {
      this.product = false;
      this.perfil = false;
      this.customer = true;
      this.cart = false;
      this.pedido = false;
      this.reloadCart = false;
      this.reloadPedido = false;
    }
    if (tab === 'cart') {
      this.product = false;
      this.perfil = false;
      this.customer = false;
      this.cart = true;
      this.pedido = false;
      this.reloadCart = true;
      this.reloadPedido = false;
    }
    if (tab === 'pedido') {
      this.product = false;
      this.perfil = false;
      this.customer = false;
      this.cart = false;
      this.pedido = true;
      this.reloadCart = false;
      this.reloadPedido = true;
    }
  }
}
