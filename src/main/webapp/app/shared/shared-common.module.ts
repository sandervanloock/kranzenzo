import { NgModule } from '@angular/core';

import { FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, KranzenzoSharedLibsModule } from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { EqualValidatorDirective } from 'app/shared/util/equal-validator.directive';
import { FloorPipe } from 'app/shared/util/floor.pipe';

@NgModule({
    imports: [KranzenzoSharedLibsModule],
    declarations: [
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective,
        FloorPipe
    ],
    exports: [
        KranzenzoSharedLibsModule,
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective,
        FloorPipe
    ]
})
export class KranzenzoSharedCommonModule {}
