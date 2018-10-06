import { NgModule } from '@angular/core';

import { FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, KranzenzoSharedLibsModule } from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { EqualValidatorDirective } from 'app/shared/util/equal-validator.directive';
import { FloorPipe } from 'app/shared/util/floor.pipe';
import { ImageUploadComponent } from 'app/shared/image/image-upload/image-upload.component';
import { FileSelectDirective } from 'ng2-file-upload';

@NgModule({
    imports: [KranzenzoSharedLibsModule],
    declarations: [
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective,
        FloorPipe,
        ImageUploadComponent,
        FileSelectDirective
    ],
    exports: [
        KranzenzoSharedLibsModule,
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective,
        FloorPipe,
        ImageUploadComponent,
        FileSelectDirective
    ]
})
export class KranzenzoSharedCommonModule {}
