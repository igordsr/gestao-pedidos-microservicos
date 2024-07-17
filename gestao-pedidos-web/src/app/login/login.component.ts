import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Auth, LoginResponse } from '../auth/service/login.model';
import { LoginService } from '../auth/service/login.service';
import { LoadingComponent } from '../shared/components/loading/loading.component';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    NgbModule,
    LoadingComponent,
  ],
  providers: [LoginService],
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, OnDestroy {
  public loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  private subscription: Subscription = new Subscription();
  public isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {}

  navigateToCadastro() {
    this.router.navigate(['/cadastro']);
  }

  login() {
    this.isLoading = true;
    this.subscription.add(
      this.loginService.login(this.loginForm.value).subscribe({
        next: (res: LoginResponse) => {
          if (!res.token) return;
          localStorage.setItem('token', res.token);

          this.subscription.add(
            this.loginService.auth().subscribe({
              next: (res: Auth) => {
                localStorage.setItem('auth', JSON.stringify(res));
                this.router.navigate(['/home']);
              },
            })
          );
        },
        error: () => {
          this.isLoading = false;
        },
        complete: () => (this.isLoading = false),
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
