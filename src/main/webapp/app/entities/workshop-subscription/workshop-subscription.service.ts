import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {WorkshopSubscription} from './workshop-subscription.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class WorkshopSubscriptionService {

    private resourceUrl = SERVER_API_URL + 'api/workshop-subscriptions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/workshop-subscriptions';

    constructor( private http: Http, private dateUtils: JhiDateUtils ) {
    }

    create( workshopSubscription: WorkshopSubscription ): Observable<WorkshopSubscription> {
        const copy = this.convert( workshopSubscription );
        return this.http.post( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    update( workshopSubscription: WorkshopSubscription ): Observable<WorkshopSubscription> {
        const copy = this.convert( workshopSubscription );
        return this.http.put( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    find( id: number ): Observable<WorkshopSubscription> {
        return this.http.get( `${this.resourceUrl}/${id}` ).map( ( res: Response ) => {
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
     * Convert a returned JSON object to WorkshopSubscription.
     */
    private convertItemFromServer( json: any ): WorkshopSubscription {
        const entity: WorkshopSubscription = Object.assign( new WorkshopSubscription(), json );
        entity.created = this.dateUtils
            .convertDateTimeFromServer( json.created );
        return entity;
    }

    /**
     * Convert a WorkshopSubscription to a JSON which can be sent to the server.
     */
    private convert( workshopSubscription: WorkshopSubscription ): WorkshopSubscription {
        const copy: WorkshopSubscription = Object.assign( {}, workshopSubscription );

        copy.created = this.dateUtils.toDate( workshopSubscription.created );
        return copy;
    }
}
