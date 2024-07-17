import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoadingService } from '../../services/loading.service';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  template: `
    <div class="loading-overlay">
      <mat-spinner></mat-spinner>
    </div>
  `,
  styles: [
    `
      .loading-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1000;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoadingComponent {
  constructor(public loadingService: LoadingService) {
    loadingService.loading$.subscribe((res) => {
      console.log(res)
    })
  }
}
