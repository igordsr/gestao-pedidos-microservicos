import { Component, Inject, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Product } from '../../../shared/model/product.model';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
@Component({
  selector: 'app-modal-product',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
  ],
  templateUrl: './modal-product.component.html',
  styleUrl: './modal-product.component.scss',
})
export class ModalProductComponent implements OnInit {
  produtoForm: FormGroup;
  constructor(
    public dialogRef: MatDialogRef<ModalProductComponent>,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { produto?: Product }
  ) {
    this.produtoForm = this.fb.group({
      id: [''],
      nome: ['', [Validators.required, Validators.minLength(1)]],
      descricao: ['', [Validators.required, Validators.minLength(1)]],
      preco: ['', [Validators.required, Validators.min(0)]],
      qtdEstoque: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    if (this.data.produto) {
      this.produtoForm.patchValue(this.data.produto);
    }
  }

  save(): void {
    if (this.produtoForm.valid) {
      this.dialogRef.close(this.produtoForm.value);
    }
  }
}
