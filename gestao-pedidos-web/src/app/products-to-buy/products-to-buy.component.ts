import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Product } from '../shared/model/product.model';
import { ProductService } from '../shared/services/product.service';
import { LoadingComponent } from '../shared/components/loading/loading.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PedidoService } from '../shared/services/pedido.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-products-to-buy',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, LoadingComponent, CommonModule, MatIconModule],
  templateUrl: './products-to-buy.component.html',
  styleUrl: './products-to-buy.component.scss',
})
export class ProductsToBuyComponent implements OnInit {
  public products: Product[] = [];
  public isLoading: boolean = false;

  @Input() public fromMenu: boolean = false;

  @Output() changeTabMenu = new EventEmitter();
  @Output() setLoading = new EventEmitter();

  constructor(
    private productService: ProductService,
    private router: Router,
    private pedidoService: PedidoService,
    private matSnack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProdutos();
  }

  loadProdutos(): void {
    this.setLoading.emit(true);
    this.isLoading = true;
    this.productService.getProducts().subscribe({
      next: (data: Product[]) => {
        this.products = data;
      },

      error: () => {
        this.isLoading = false;
        this.setLoading.emit(false);
      },
      complete: () => {
        this.isLoading = false;
        this.setLoading.emit(false);
      },
    });
  }

  showMessage(message: string) {
    this.matSnack.open(message, 'Fechar', {
      duration: 3000,
    });
  }

  async getPedidos() {
    const pedidos = await this.pedidoService.getPedidos().toPromise();
    return pedidos;
  }

  async addToCart(idProduct: string, nome: string) {
    if (!localStorage.getItem('token')) this.router.navigate(['/login']);

    this.setLoading.emit(true);
    this.isLoading = true;

    const pedidos = (await this.getPedidos()) || [];

    const pedido = pedidos.find((p) => p.status === 'AGUARDANDO PAGAMENTO');
    if (pedido) {
      pedido.itemList.push({
        produto: idProduct,
        quantidade: 1,
      });

      this.pedidoService.update(pedido.identificador, pedido).subscribe({
        next: () => {
          this.showMessage(`Produto [${nome}] adicionado ao carrinho!`);
          this.changeTabMenu.emit('cart');
        },
        error: () => {
          this.isLoading = false;
          this.setLoading.emit(false);
        },
        complete: () => {
          this.isLoading = false;
          this.setLoading.emit(false);
        },
      });
    } else {
      this.pedidoService
        .register({
          identificador: '',
          cliente: JSON.parse(localStorage.getItem('auth') || '').userId,
          itemList: [
            {
              produto: idProduct,
              quantidade: 1,
            },
          ],
        })
        .subscribe({
          next: () => {
            this.showMessage(`Produto [${nome}] adicionado ao carrinho!`);
            this.changeTabMenu.emit('cart');
          },
          error: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
          complete: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
        });
    }
  }
}
