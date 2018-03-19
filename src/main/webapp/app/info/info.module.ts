import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {infoRoutes} from './info.route';
import {InfoComponent} from './info.component';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule( {
               imports: [CommonModule, RouterModule.forRoot( infoRoutes, {useHash: true} ), NgbModule], declarations: [InfoComponent]
           } )
export class KransenzoInfoModule {
}
