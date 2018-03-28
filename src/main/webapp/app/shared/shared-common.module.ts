import {LOCALE_ID, NgModule} from '@angular/core';
import {Title} from '@angular/platform-browser';

import {FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, JhiLanguageHelper, KransenzoSharedLibsModule, S3ImageResizePipe} from './';

@NgModule({
    imports: [
        KransenzoSharedLibsModule
    ],
    declarations: [
        FindLanguageFromKeyPipe, S3ImageResizePipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ],
    providers: [
        JhiLanguageHelper,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'nl'
        },
    ],
    exports: [
        KransenzoSharedLibsModule,
        FindLanguageFromKeyPipe, S3ImageResizePipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class KransenzoSharedCommonModule {}
