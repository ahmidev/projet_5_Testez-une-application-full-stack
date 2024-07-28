import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to /sessions on successful login', () => {
    const mockSessionInformation: SessionInformation =
    {
      token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhaG1pZC5haXRvdWFsaUBsYXBvc3RlLm5ldCIsImlhdCI6MTcyMjE4NTMwOSwiZXhwIjoxNzIyMjcxNzA5fQ.YYxdaN55X8b-UGbjOB2i2hbAzR0SgN3c95Rwn5Xu-nF1AUlEiHRBm8XBMxFmJNklCjXWMVyKpPYUf03tozzhfQ",
      type: "Bearer",
      id: 2,
      username: "ahmid.aitouali@laposte.net",
      firstName: "AHMID",
      lastName: "AIT OUALI",
      admin: false
    };
    jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
    jest.spyOn(sessionService, 'logIn');
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.form.controls['email'].setValue('ahmid.aitouali@laposte.net');
    component.form.controls['password'].setValue('test!123');
    component.submit();
    expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should display error message if login fails', async () => {
    const errorResponse = new HttpErrorResponse({
      error: {
        path: '/api/auth/login',
        error: 'Unauthorized',
        message: 'Bad credentials',
        status: 401
      },
      status: 401,
      statusText: 'Unauthorized'
    });

    jest.spyOn(authService, 'login').mockReturnValue(throwError(errorResponse));
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('password');
    component.submit();
    fixture.detectChanges();
    expect(component.onError).toBeTruthy();
  });


  it('should disable submit button if form is invalid', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    fixture.detectChanges();
    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBeTruthy();
  });



  it('should validate form fields', () => {
    let email = component.form.controls['email'];
    let password = component.form.controls['password'];

    email.setValue('');
    expect(email.valid).toBeFalsy();

    email.setValue('ahmid.aitouali.laposte.net');
    expect(email.valid).toBeFalsy();

    email.setValue('ahmid.aitouali@laposte.net');
    expect(email.valid).toBeTruthy();

    password.setValue('');
    expect(password.valid).toBeFalsy();

    password.setValue('12');
    expect(password.valid).toBeFalsy();

    password.setValue('123');
    expect(password.valid).toBeTruthy();
  });
});
