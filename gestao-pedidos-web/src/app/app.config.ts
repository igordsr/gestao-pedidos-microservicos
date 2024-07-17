import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './shared/interceptors/auth.interceptor';
import { loadingInterceptor } from './shared/interceptors/loading.interceptor';
import { IConfig, provideEnvironmentNgxMask } from 'ngx-mask';
const maskConfig: Partial<IConfig> = {
  validation: false,
};
export const appConfig: ApplicationConfig = {
  providers: [
    provideEnvironmentNgxMask(maskConfig),
    provideRouter(routes),
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([AuthInterceptor, loadingInterceptor])),
  ],
};
