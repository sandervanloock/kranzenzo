import {LOCALE_ID, NgModule} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {registerLocaleData} from '@angular/common';
import locale from '@angular/common/locales/nl';

import {FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, JhiLanguageHelper, KransenzoSharedLibsModule} from './';

@NgModule( {
               imports: [KransenzoSharedLibsModule], declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent], providers: [JhiLanguageHelper, Title, {
        provide: LOCALE_ID, useValue: 'nl'
    },], exports: [KransenzoSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent]
           } )
export class KransenzoSharedCommonModule {
    constructor() {
        registerLocaleData( locale );
    }
}
