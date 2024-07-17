import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
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
@Component({
  selector: 'app-products',
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
  providers: [ProductService],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss',
})
export class ProductsComponent implements OnInit {
  @ViewChild('fileInput', { static: false }) fileInput!: ElementRef;

  @Input() type: string | null = null;
  @Output() setLoading = new EventEmitter();
  constructor(
    private dialog: MatDialog,
    private productService: ProductService,
    private matSnack: MatSnackBar
  ) {}

  rowDef = ['nome', 'descricao', 'preco', 'qtdEstoque'];
  products: Product[] = [];
  selectedFile: File | null = null;

  ngOnInit(): void {
    if (this.type === 'ADMIN') this.rowDef.push('actions');
    this.loadProdutos();
  }

  editProduto(produto: Product): void {
    this.openProductForm(produto);
  }

  deleteProduto(id: string): void {
    this.dialog
      .open(ModalDeleteComponent, {
        width: '250px',
      })
      .afterClosed()
      .subscribe((res) => {
        if (!res) return;
        this.setLoading.emit(true);
        this.productService.deleteProduct(id).subscribe({
          next: () => {
            this.loadProdutos('Excluído com sucesso!');
          },
          error: () => this.setLoading.emit(false),
        });
      });
  }

  showMessage(message: string) {
    this.matSnack.open(message, 'Fechar', {
      duration: 3000,
    });
  }

  loadProdutos(message?: string): void {
    this.setLoading.emit(true);
    this.productService.getProducts().subscribe({
      next: (data: Product[]) => {
        this.products = data;
        if (message) this.showMessage(message);
      },
      error: () => this.setLoading.emit(false),
      complete: () => this.setLoading.emit(false),
    });
  }

  openProductForm(produto?: Product): void {
    const dialogRef = this.dialog.open(ModalProductComponent, {
      data: { produto: produto },
      width: '400px',
    });

    dialogRef.afterClosed().subscribe((result: Product) => {
      if (result) {
        if (result.id) {
          this.setLoading.emit(true);
          this.productService.updateProduct(result.id, result).subscribe({
            next: () => this.loadProdutos('Atualizado com sucesso!'),
          });
        } else {
          this.setLoading.emit(true);
          this.productService.addProduct(result).subscribe({
            next: () => this.loadProdutos('Cadastrado com sucesso!'),
            error: () => this.setLoading.emit(false),
          });
        }
      }
    });
  }
  
  openFileDialog() {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.productService.upload(this.selectedFile).subscribe(()=>{});
    }
  }
}
