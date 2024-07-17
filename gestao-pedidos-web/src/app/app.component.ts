import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoadingService } from './shared/services/loading.service';
import { LoadingComponent } from './shared/components/loading/loading.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingComponent],
  providers: [LoadingService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'gestao-pedidos.web';
}
