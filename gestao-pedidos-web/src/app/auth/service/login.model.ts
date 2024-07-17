export interface Authorities {
  authority: string;
}

export interface Auth {
  authorities: Authorities[];
  userId: string;
  username: string;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
}

export interface LoginResponse {
  token: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}
