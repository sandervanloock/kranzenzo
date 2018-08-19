import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {Workshop} from './workshop.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class WorkshopService {

    private resourceUrl = SERVER_API_URL + 'api/workshops';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/workshops';

    constructor( private http: Http ) {
    }

    create( workshop: Workshop ): Observable<Workshop> {
        const copy = this.convert( workshop );
        return this.http.post( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    update( workshop: Workshop ): Observable<Workshop> {
        const copy = this.convert( workshop );
        return this.http.put( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    find( id: number, filterDates?: boolean ): Observable<Workshop> {
        return this.http.get( `${this.resourceUrl}/${id}?filterDates=${filterDates ? filterDates : false}` ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    query( req?: any ): Observable<ResponseWrapper> {
        const options = createRequestOption( req );
        return this.http.get( this.resourceUrl, options )
            .map( ( res: Response ) => this.convertResponse( res ) );
    }

    delete( id: number ): Observable<Response> {
        return this.http.delete( `${this.resourceUrl}/${id}` );
    }

    search( req?: any ): Observable<ResponseWrapper> {
        const options = createRequestOption( req );
        return this.http.get( this.resourceSearchUrl, options )
            .map( ( res: any ) => this.convertResponse( res ) );
    }

    private convertResponse( res: Response ): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for ( let i = 0; i < jsonResponse.length; i++ ) {
            result.push( this.convertItemFromServer( jsonResponse[i] ) );
        }
        return new ResponseWrapper( res.headers, result, res.status );
    }

    /**
     * Convert a returned JSON object to Workshop.
     */
    private convertItemFromServer( json: any ): Workshop {
        const entity: Workshop = Object.assign( new Workshop(), json );
        return entity;
    }

    /**
     * Convert a Workshop to a JSON which can be sent to the server.
     */
    private convert( workshop: Workshop ): Workshop {
        const copy: Workshop = Object.assign( {}, workshop );
        return copy;
    }
}
