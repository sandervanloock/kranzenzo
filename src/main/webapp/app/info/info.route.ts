import {Routes} from '@angular/router';
import {InfoComponent} from './info.component';

export const infoRoutes: Routes = [{
    path: 'info', data: {pageTitle: 'info.title'}, canActivate: [], component: InfoComponent
}];
