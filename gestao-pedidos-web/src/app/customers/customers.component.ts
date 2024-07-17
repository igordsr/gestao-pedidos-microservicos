import { Component, OnInit } from '@angular/core';
import { UserService } from '../shared/services/user.service';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from '../shared/components/loading/loading.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { User } from '../shared/model/user.model';
import { ProductService } from '../shared/services/product.service';
import { ModalDeleteComponent } from '../home/components/modal-delete/modal-delete.component';
@Component({
  selector: 'app-customers',
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
  ],
  providers: [UserService],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss',
})
export class CustomersComponent implements OnInit {
  customers: User[] = [];
  isLoading = false;

  constructor(
    private dialog: MatDialog,
    private productService: ProductService,
    private matSnack: MatSnackBar,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.isLoading = true;
    this.userService.getUsers().subscribe({
      next: (users: User[]) =>
        (this.customers = users.filter((r) => r.role === 'ROLE_CLIENTE')),
      error: () => (this.isLoading = false),
      complete: () => (this.isLoading = false),
    });
  }

  deleteCustomer(id: string) {
    this.dialog
      .open(ModalDeleteComponent, {
        width: '250px',
      })
      .afterClosed()
      .subscribe((res) => {
        if (!res) return;
        this.isLoading = true;
        this.userService.delete(id).subscribe({
          next: () => this.loadCustomers(),
          error: () => (this.isLoading = false),
          complete: () => (this.isLoading = false),
        });
      });
  }
}
