import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class MailService {
  private readonly API_URL = environment.apiMailUrl + '/mail';

  constructor(private http: HttpClient) {}

  requestPasswordReset(email: string): Observable<any> {
    return this.http.post(`${this.API_URL}/password-reset/request`, { email });
  }

  validateResetToken(token: string): Observable<any> {
    return this.http.post(`${this.API_URL}/password-reset/validate`, { token });
  }

  confirmPasswordReset(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.API_URL}/password-reset/confirm`, { token, newPassword });
  }

  sendEmail(to: string, subject: string, body: string): Observable<any> {
    return this.http.post(`${this.API_URL}/send`, { to, subject, body });
  }
}
