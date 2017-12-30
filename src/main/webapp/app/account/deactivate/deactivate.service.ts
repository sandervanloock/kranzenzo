import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';
import {Http, Response, URLSearchParams} from '@angular/http';

@Injectable()
export class DeactivateService {

    constructor( private http: Http ) {
    }

    get( email: string ): Observable<any> {
        const params: URLSearchParams = new URLSearchParams();
        params.set( 'email', email );

        return this.http.get( SERVER_API_URL + 'api/deactivate', {
                                                                     search: params
                                                                 } ).map( ( res: Response ) => res );
    }
}
