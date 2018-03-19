import {Routes} from '@angular/router';
import {InfoComponent} from './info.component';

export const infoRoutes: Routes = [{
    path: 'info', data: {}, canActivate: [], component: InfoComponent
}];
