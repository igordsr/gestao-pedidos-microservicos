import { inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

import { HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);
  const router = inject(Router);

  const token =
    typeof window !== 'undefined' && typeof window.document !== 'undefined'
      ? localStorage.getItem('token') || null
      : null;
  let authReq = req;

  if (
    token &&
    !(req.method === 'POST' && req.url?.includes('usuario')) &&
    !(req.method === 'POST' && req.url?.includes('auth'))
  ) {
    authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMsg = 'Ocorreu um erro desconhecido!';

      if (error.error instanceof ErrorEvent) {
        errorMsg = `Erro: ${error.error.message}`;
      } else {
        if (error.status === 401) {
          errorMsg = 'Não autorizado! Por favor, efetue o login novamente.';
        } else if (error.status === 403) {
          errorMsg = 'Forbidden! Você não possui autorização.';
          router.navigate(['/login']);
        } else {
          errorMsg = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        if (error.status === 400 && error.url?.includes('auth')) {
          errorMsg = 'Email ou senha inválido!';
        } else if (error.error.message) errorMsg = error.error.message;
      }

      snackBar.open(errorMsg, 'Fechar', {
        duration: 3000,
      });

      return throwError(() => new Error(errorMsg));
    })
  );
};
