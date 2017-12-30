import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../shared';

import {
    accountState, ActivateComponent,
    ActivateService, PasswordComponent, PasswordResetFinishComponent,
    PasswordResetFinishService, PasswordResetInitComponent, PasswordResetInitService, PasswordService,
    PasswordStrengthBarComponent, Register,
    RegisterComponent,
    SettingsComponent,
    SocialAuthComponent, SocialRegisterComponent
} from './';
import {DeactivateService} from './deactivate/deactivate.service';

@NgModule({
    imports: [
        KransenzoSharedModule,
        RouterModule.forChild(accountState)
    ],
    declarations: [
        SocialRegisterComponent,
        SocialAuthComponent,
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent
    ],
    providers: [
        Register,
        ActivateService, DeactivateService,
        PasswordService,
        PasswordResetInitService,
        PasswordResetFinishService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KransenzoAccountModule {}
