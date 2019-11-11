import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared/shared.module';
import { WorkshopDateComponent } from './workshop-date.component';
import { WorkshopDateDetailComponent } from './workshop-date-detail.component';
import { WorkshopDateUpdateComponent } from './workshop-date-update.component';
import { WorkshopDateDeletePopupComponent, WorkshopDateDeleteDialogComponent } from './workshop-date-delete-dialog.component';
import { workshopDateRoute, workshopDatePopupRoute } from './workshop-date.route';

const ENTITY_STATES = [...workshopDateRoute, ...workshopDatePopupRoute];

@NgModule({
  imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    WorkshopDateComponent,
    WorkshopDateDetailComponent,
    WorkshopDateUpdateComponent,
    WorkshopDateDeleteDialogComponent,
    WorkshopDateDeletePopupComponent
  ],
  entryComponents: [WorkshopDateDeleteDialogComponent]
})
export class KranzenzoWorkshopDateModule {}
