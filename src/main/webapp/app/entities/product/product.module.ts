import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FileSelectDirective } from 'ng2-file-upload';

import { KranzenzoSharedModule } from 'app/shared';
import {
    ProductComponent,
    ProductDeleteDialogComponent,
    ProductDeletePopupComponent,
    ProductDetailComponent,
    productPopupRoute,
    productRoute,
    ProductUpdateComponent
} from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';

const ENTITY_STATES = [...productRoute, ...productPopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductComponent,
        ProductDetailComponent,
        ProductUpdateComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
        FileSelectDirective
    ],
    entryComponents: [ProductComponent, ProductUpdateComponent, ProductDeleteDialogComponent, ProductDeletePopupComponent],
    providers: [S3ImageResizePipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoProductModule {}
