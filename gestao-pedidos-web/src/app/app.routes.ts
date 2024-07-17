import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CadastroComponent } from './cadastro/cadastro.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './auth/guard/auth.guard';
import { HomepageComponent } from './homepage/homepage.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'homepage', component: HomepageComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: '**', redirectTo: '/homepage' },
];
