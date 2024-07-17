import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatOptionModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core'; // Importa o MatNativeDateModule
import { MatSelectModule } from '@angular/material/select';
import { Router } from '@angular/router';
import { UserService } from '../shared/services/user.service';
import { Subscription } from 'rxjs';
import { User } from '../shared/model/user.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LoadingComponent } from '../shared/components/loading/loading.component';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatCardModule,
    MatOptionModule,
    MatDatepickerModule,
    MatFormFieldModule,
    NgbModule,
    MatInputModule,
    MatSelectModule,
    MatNativeDateModule,
    MatButtonModule,
    MatSnackBarModule,
    LoadingComponent,
    NgxMaskDirective,
    NgxMaskPipe,
  ],
  templateUrl: './cadastro.component.html',
  providers: [UserService, provideNgxMask()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CadastroComponent implements OnInit, OnDestroy {
  @Input() isPerfil = false;
  @Input() user: User | undefined;

  @Output() setLoading = new EventEmitter();

  public userId: string = '';

  public cadastroForm: FormGroup = this.fb.group({
    nome: ['', [Validators.required]],
    cep: ['', [Validators.required, Validators.pattern('\\d{5}-\\d{3}')]],
    logradouro: ['', [Validators.required]],
    complemento: [''],
    bairro: ['', [Validators.required]],
    numero: ['', [Validators.required]],
    telefone: ['', [Validators.required, Validators.pattern('\\d{10,11}')]],
    email: ['', [Validators.required, Validators.email]],
    dataNascimento: ['', [Validators.required]],
    cpf: ['', [Validators.required, Validators.pattern('\\d{11}')]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['', [Validators.required]],
  });

  private subscription: Subscription = new Subscription();
  public isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private userSevice: UserService
  ) {}

  ngOnInit(): void {
    if (this.isPerfil) {
      this.isLoading = true;
      this.setLoading.emit(true);
      this.userId = JSON.parse(localStorage.getItem('auth') || '')?.userId;
      this.userSevice.getUser(this.userId).subscribe({
        next: (res: User) => {
          this.user = res;
          this.cadastroForm.patchValue(this.user);
          this.cadastroForm.get('email')?.disable();
          this.cadastroForm.get('cpf')?.disable();
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

  viaCep() {
    const cep = this.cadastroForm.get('cep')?.value;
    if (cep.length < 8) return;

    this.isLoading = true;
    this.setLoading.emit(true);

    this.userService.getCep(cep).subscribe({
      next: (res: User) => {
        this.cadastroForm.patchValue(res);
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

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  clear() {
    if (!this.user) return;
    this.cadastroForm.patchValue(this.user);
    this.cadastroForm.get('password')?.setValue(null);
  }

  update() {
    this.isLoading = true;
    this.setLoading.emit(true);

    const payload = this.cadastroForm.getRawValue();
    this.subscription.add(
      this.userService
        .update(this.userId, {
          ...payload,
          dataNascimento: payload.dataNascimento.toISOString().split('T')[0],
        })
        .subscribe({
          next: (res: User) => {
            if (!res) return;
            this.snackBar.open('Cadastro atualizado com sucesso!');
          },
          error: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
          complete: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
        })
    );
  }

  register() {
    this.isLoading = true;
    this.setLoading.emit(true);

    const payload = this.cadastroForm.getRawValue();
    this.subscription.add(
      this.userService
        .register({
          ...payload,
          dataNascimento: payload.dataNascimento.toISOString().split('T')[0],
        })
        .subscribe({
          next: (res: User) => {
            if (!res) return;
            this.snackBar.open('Cadastro realizado com sucesso!', 'Fechar', {
              duration: 3000,
            });
            this.navigateToLogin();
          },
          error: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
          complete: () => {
            this.isLoading = false;
            this.setLoading.emit(false);
          },
        })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
