import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { Product } from '../../../shared/model/product.model';
import { ProductService } from '../../../shared/services/product.service';
import { ModalProductComponent } from '../modal-product/modal-product.component';
import { CommonModule } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { subscribe } from 'diagnostics_channel';
import { LoadingComponent } from '../../../shared/components/loading/loading.component';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { Pedido } from '../../../shared/model/pedido.model';
import { PedidoService } from '../../../shared/services/pedido.service';
import { asyncScheduler } from 'rxjs';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule,
    FormsModule,
    LoadingComponent,
    ModalDeleteComponent,
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit, OnChanges {
  @Input() type: string | null = null;
  @Input() public reloadCart: boolean = false;
  @Output() setLoading = new EventEmitter();
  @Output() changeTabMenu = new EventEmitter();
  public pedidoMain: Pedido[] = [];
  public isLoading: boolean = true;
  total = 0;
  constructor(
    private dialog: MatDialog,
    private pedidoService: PedidoService,
    private matSnack: MatSnackBar,
    private productService: ProductService
  ) {}

  rowDef = ['nome', 'descricao', 'preco', 'quantidade', 'actions'];
  pedidos: any[] = [];
  selectedFile: File | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['reloadCart'].currentValue) {
      this.loadCart();
    }
  }

  ngOnInit(): void {
    this.loadCart();
  }

  comprar() {
    this.changeTabMenu.emit('product');
  }

  onPay() {
    this.setLoading.emit(true);

    this.pedidoService.payment(this.pedidoMain[0].identificador).subscribe({
      next: () => {
        this.loadCart('Pagamento efetuada com sucesso!');
        this.changeTabMenu.emit('pedido');
      },
      complete: () => this.setLoading.emit(false),

      error: () => this.setLoading.emit(false),
    });
  }

  onDelete(idPedido: string, idProduct: string): void {
    this.dialog
      .open(ModalDeleteComponent, {
        width: '250px',
      })
      .afterClosed()
      .subscribe((res) => {
        if (!res) return;
        this.setLoading.emit(true);

        this.pedidoMain.forEach((pedido) => {
          if (pedido.identificador === idPedido) {
            pedido.itemList = pedido.itemList.filter(
              (item) => item.produto !== idProduct
            );
            this.pedidoService.update(idPedido, pedido).subscribe({
              next: () => {
                this.loadCart('Excluído com sucesso!');
              },
              error: () => this.setLoading.emit(false),
            });
          }
        });
      });
  }

  showMessage(message: string) {
    this.matSnack.open(message, 'Fechar', {
      duration: 3000,
    });
  }

  setQtd(idPedido: string, idProduct: string, qtd: number) {
    this.setLoading.emit(true);
    this.pedidoMain.forEach((pedido, i) => {
      if (pedido.identificador === idPedido) {
        pedido.itemList.forEach((item, j) => {
          if (item.produto === idProduct) {
            this.pedidoMain[i].itemList[j].quantidade = qtd;
          }
        });

        this.pedidoService.update(idPedido, pedido).subscribe({
          next: () => this.loadCart(),
          error: () => this.setLoading.emit(false),
        });
      }
    });
  }

  incrementar(idPedido: string, idProduct: string, qtd: number) {
    this.setQtd(idPedido, idProduct, qtd + 1);
  }

  decrementar(idPedido: string, idProduct: string, qtd: number) {
    if (qtd === 1) {
      this.onDelete(idPedido, idProduct);
      return;
    }

    this.setQtd(idPedido, idProduct, qtd - 1);
  }

  loadCart(message?: string): void {
    this.total = 0;
    this.isLoading = true;
    this.setLoading.emit(true);
    this.pedidos = [];
    this.pedidoService.getPedidos().subscribe({
      next: async (data: Pedido[]) => {
        if (data.length === 0) {
          this.isLoading = false;
          this.setLoading.emit(false);
          if (message) this.showMessage(message);
          return;
        }

        data = data.filter(
          (pedido) => pedido.status === 'AGUARDANDO PAGAMENTO'
        );
        this.pedidoMain = data;

        for (let i = 0; i < data.length; i++) {
          const pedido = data[i];
          for (let j = 0; j < pedido.itemList.length; j++) {
            this.setLoading.emit(true);
            const item = pedido.itemList[j];

            const p = await this.productService
              .getProduct(item.produto)
              .toPromise();

            if (p?.preco)
              this.total = (this.total || 0) + p?.preco * item.quantidade;

            this.pedidos.push({
              ...p,
              idPedido: pedido.identificador,
              quantidade: item.quantidade,
            });
          }
        }

        if (message) this.showMessage(message);
        asyncScheduler.schedule(() => {
          this.isLoading = false;
          this.setLoading.emit(false);
        }, 1000);
      },
      error: () => this.setLoading.emit(false),
    });
  }
}
