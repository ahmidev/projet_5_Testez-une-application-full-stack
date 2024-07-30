import { HttpClientModule, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { NgZone } from '@angular/core';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        AuthService,
        FormBuilder
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes([])
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register and navigate to /login on successful registration', () => {
    const mockRegisterRequest = {
      email: "ahmid.aitouali@laposte.net",
      firstName: "AHMID",
      lastName: "AIT OUALI",
      password: 'password123'
    };

    jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.controls['email'].setValue(mockRegisterRequest.email);
    component.form.controls['firstName'].setValue(mockRegisterRequest.firstName);
    component.form.controls['lastName'].setValue(mockRegisterRequest.lastName);
    component.form.controls['password'].setValue(mockRegisterRequest.password);
    ngZone.run(() => {
      component.submit();
    });
    expect(authService.register).toHaveBeenCalledWith(mockRegisterRequest);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should display error message if registration fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: null,
      status: 400,
      statusText: 'Bad Request',
      url: 'http://localhost:4200/api/auth/register',
      headers: new HttpHeaders()
    });

    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => errorResponse));
    
    component.form.controls['email'].setValue('ahmid.aitouali@laposte.net');
    component.form.controls['firstName'].setValue('AHMID');
    component.form.controls['lastName'].setValue('AIT OUALI');
    component.form.controls['password'].setValue('password123');
    component.submit();

    fixture.detectChanges();

    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
    expect(component.onError).toBeTruthy();
  });

  it('should invalidate the form if required fields are missing', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('');
    component.form.controls['password'].setValue('');
    fixture.detectChanges();

    expect(component.form.invalid).toBeTruthy();
    
    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBeTruthy();
  });

  it('should validate form fields', () => {
    let email = component.form.controls['email'];
    let firstName = component.form.controls['firstName'];
    let lastName = component.form.controls['lastName'];
    let password = component.form.controls['password'];

    // Test email field
    email.setValue('');
    expect(email.valid).toBeFalsy();

    email.setValue('ahmid.aitouali.laposte.net');
    expect(email.valid).toBeFalsy();

    email.setValue('ahmid.aitouali@laposte.net');
    expect(email.valid).toBeTruthy();

    // Test firstName field
    firstName.setValue('');
    expect(firstName.valid).toBeFalsy();

    firstName.setValue('Ah');
    expect(firstName.valid).toBeFalsy();

    firstName.setValue('Ahmiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiid');
    expect(firstName.valid).toBeFalsy();

    firstName.setValue('Ahmid');
    expect(firstName.valid).toBeTruthy();

    // Test lastName field
    lastName.setValue('');
    expect(lastName.valid).toBeFalsy();

    lastName.setValue('AI');
    expect(lastName.valid).toBeFalsy();

    lastName.setValue('AIT OUALIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII');
    expect(lastName.valid).toBeFalsy();

    lastName.setValue('AIT OUALI');
    expect(lastName.valid).toBeTruthy();

    // Test password field
    password.setValue('');
    expect(password.valid).toBeFalsy();

    password.setValue('12');
    expect(password.valid).toBeFalsy();

    password.setValue('12345678901234567890123456789012345678901');
    expect(password.valid).toBeFalsy();

    password.setValue('123');
    expect(password.valid).toBeTruthy();
  });


});
