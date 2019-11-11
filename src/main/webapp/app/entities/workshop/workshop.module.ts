import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared/shared.module';
import { WorkshopComponent } from './workshop.component';
import { WorkshopDetailComponent } from './workshop-detail.component';
import { WorkshopUpdateComponent } from './workshop-update.component';
import { WorkshopDeletePopupComponent, WorkshopDeleteDialogComponent } from './workshop-delete-dialog.component';
import { workshopRoute, workshopPopupRoute } from './workshop.route';

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
  entryComponents: [WorkshopDeleteDialogComponent]
})
export class KranzenzoWorkshopModule {}
