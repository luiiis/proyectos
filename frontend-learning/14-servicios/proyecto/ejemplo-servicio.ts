// Angular Services - Reference File
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  // Inject dependencies with inject()
  private http = inject(HttpClient);

  // BehaviorSubject for reactive state management
  private usersSubject = new BehaviorSubject<User[]>([]);
  public users$ = this.usersSubject.asObservable();

  private apiUrl = '/api/users';

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl).pipe(
      tap(users => this.usersSubject.next(users))
    );
  }

  addUser(user: Partial<User>): Observable<User> {
    return this.http.post<User>(this.apiUrl, user).pipe(
      tap(newUser => {
        const current = this.usersSubject.getValue();
        this.usersSubject.next([...current, newUser]);
      })
    );
  }

  getCurrentUsers(): User[] {
    return this.usersSubject.getValue();
  }
}

console.log('Reference file: ejemplo-servicio.ts - Services & DI patterns for use in an Angular project');
