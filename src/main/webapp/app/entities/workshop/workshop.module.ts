import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared';
import {
    WorkshopComponent,
    WorkshopDeleteDialogComponent,
    WorkshopDeletePopupComponent,
    WorkshopDetailComponent,
    workshopPopupRoute,
    workshopRoute,
    WorkshopUpdateComponent
} from './';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';

const ENTITY_STATES = [...workshopRoute, ...workshopPopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WorkshopComponent,
        WorkshopDetailComponent,
        WorkshopUpdateComponent,
        WorkshopDeleteDialogComponent,
        WorkshopDeletePopupComponent
    ],
    entryComponents: [WorkshopComponent, WorkshopUpdateComponent, WorkshopDeleteDialogComponent, WorkshopDeletePopupComponent],
    providers: [S3ImageResizePipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoWorkshopModule {}
