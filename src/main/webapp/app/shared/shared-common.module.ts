import { NgModule } from '@angular/core';

import { FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, KranzenzoSharedLibsModule } from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { EqualValidatorDirective } from 'app/shared/util/equal-validator.directive';
import { FloorPipe } from 'app/shared/util/floor.pipe';
import { ImageUploadComponent } from 'app/shared/image/image-upload/image-upload.component';
import { FileUploadModule } from 'ng2-file-upload';
import { ConfirmationDialogComponent } from 'app/shared/dialog/confirmation-dialog.component';
import { ProgressSpinnerDialogComponent } from 'app/shared/dialog/progress-spinner-dialog.component';

@NgModule({
    imports: [KranzenzoSharedLibsModule, FileUploadModule],
    declarations: [
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        S3ImageResizePipe,
        EqualValidatorDirective,
        FloorPipe,
        ImageUploadComponent,
        ConfirmationDialogComponent,
        ProgressSpinnerDialogComponent
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
        ConfirmationDialogComponent,
        ProgressSpinnerDialogComponent
    ]
})
export class KranzenzoSharedCommonModule {}
