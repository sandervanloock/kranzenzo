import { NgModule } from '@angular/core';

import { FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, KranzenzoSharedLibsModule } from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { EqualValidatorDirective } from 'app/shared/util/equal-validator.directive';

@NgModule({
    imports: [KranzenzoSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, S3ImageResizePipe, EqualValidatorDirective],
    exports: [
        KranzenzoSharedLibsModule,
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective
    ]
})
export class KranzenzoSharedCommonModule {}
