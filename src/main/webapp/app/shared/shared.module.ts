import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {DatePipe} from '@angular/common';

import {
    AccountService,
    AuthServerProvider,
    CSRFService,
    HasAnyAuthorityDirective,
    JhiLoginModalComponent,
    JhiSocialComponent,
    KransenzoSharedCommonModule,
    KransenzoSharedLibsModule,
    LoginModalService,
    LoginService,
    Principal,
    SocialService,
    StateStorageService,
    UserService,
} from './';

@NgModule( {
               imports: [KransenzoSharedLibsModule, KransenzoSharedCommonModule],
               declarations: [JhiSocialComponent, JhiLoginModalComponent, HasAnyAuthorityDirective],
               providers: [LoginService, LoginModalService, AccountService, StateStorageService, Principal, CSRFService, AuthServerProvider, SocialService, UserService, DatePipe],
               entryComponents: [JhiLoginModalComponent],
               exports: [KransenzoSharedCommonModule, JhiSocialComponent, JhiLoginModalComponent, HasAnyAuthorityDirective, DatePipe],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]

           } )
export class KransenzoSharedModule {
}
