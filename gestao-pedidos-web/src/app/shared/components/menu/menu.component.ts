import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  OnInit,
  Output,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { Auth } from '../../../auth/service/login.model';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MenuComponent implements OnInit {
  @Output() perfilClick = new EventEmitter();
  @Output() productClick = new EventEmitter();
  @Output() customerClick = new EventEmitter();
  @Output() cartClick = new EventEmitter();
  @Output() pedidoClick = new EventEmitter();

  public username: string = '';
  public type: string = '';

  constructor(private router: Router, private userSevice: UserService) {}

  ngOnInit(): void {
    const auth: Auth = JSON.parse(localStorage.getItem('auth') || '');
    this.username = auth.username;
    this.type = auth.authorities[0].authority.split('_')[1];
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('auth');

    this.router.navigate(['/login']);
  }
}
