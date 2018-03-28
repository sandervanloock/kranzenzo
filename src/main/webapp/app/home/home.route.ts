import {HomeComponent} from './';
import {Route} from '@angular/router';

export const HOME_ROUTE: Route[] = [{
    path: '', component: HomeComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}];
