import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { AuthService } from './features/auth/services/auth.service';


describe('AppComponent', () => {
  let router: Router;
  let sessionService: SessionService;

  const mockSessionService = {
    isLogged: false,
    sessionInformation: undefined,
    $isLogged: jest.fn().mockReturnValue(of(false)),
    logOut: jest.fn()
  };
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: {} }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should log out and navigate to the home page', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const navigateSpy = jest.spyOn(router, 'navigate');

    app.logout();

    expect(sessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
