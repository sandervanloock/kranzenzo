import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';

import {Order} from './order.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class OrderService {

    private resourceUrl = 'api/orders';
    private resourceSearchUrl = 'api/_search/orders';

    constructor( private http: Http, private dateUtils: JhiDateUtils ) {
    }

    create( order: Order ): Observable<Order> {
        const copy = this.convert( order );
        return this.http.post( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            this.convertItemFromServer( jsonResponse );
            return jsonResponse;
        } );
    }

    update( order: Order ): Observable<Order> {
        const copy = this.convert( order );
        return this.http.put( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            this.convertItemFromServer( jsonResponse );
            return jsonResponse;
        } );
    }

    find( id: number ): Observable<Order> {
        return this.http.get( `${this.resourceUrl}/${id}` ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            this.convertItemFromServer( jsonResponse );
            return jsonResponse;
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
        for ( let i = 0; i < jsonResponse.length; i++ ) {
            this.convertItemFromServer( jsonResponse[i] );
        }
        return new ResponseWrapper( res.headers, jsonResponse, res.status );
    }

    private convertItemFromServer( entity: any ) {
        entity.created = this.dateUtils
            .convertDateTimeFromServer( entity.created );
        entity.updated = this.dateUtils
            .convertDateTimeFromServer( entity.updated );
    }

    private convert( order: Order ): Order {
        const copy: Order = Object.assign( {}, order );

        copy.created = this.dateUtils.toDate( order.created );

        copy.updated = this.dateUtils.toDate( order.updated );
        return copy;
    }
}
