import { NgModule } from '@angular/core';
import { HomepageComponent } from './homepage.component';
import { homepageSettingRoute } from 'app/entities/homepage/hompage.route';
import { KranzenzoSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';

const ENTITY_STATES = homepageSettingRoute;

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HomepageComponent],
    entryComponents: [HomepageComponent]
})
export class HomepageModule {}
