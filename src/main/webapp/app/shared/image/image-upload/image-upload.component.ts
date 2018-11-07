import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FileUploader } from 'ng2-file-upload/ng2-file-upload';
import { IImage, Image } from '../../model/image.model';
import { AuthServerProvider } from '../../../core/auth/auth-jwt.service';
import { S3ImageResizePipe } from '../../util/s3-image-resize.pipe';

@Component({
    selector: 'jhi-image-upload',
    templateUrl: './image-upload.component.html',
    styleUrls: ['./image-upload.css']
})
export class ImageUploadComponent implements OnInit {
    uploader: FileUploader;
    imageEndpoints: string[] = [];

    @Output() imagesEmitter = new EventEmitter<IImage[]>();
    private _images: IImage[];

    constructor(private authServer: AuthServerProvider, private s3ImageResizePipe: S3ImageResizePipe) {}

    ngOnInit() {
        const headers = [
            {
                name: 'Authorization',
                value: `Bearer ${this.authServer.getToken()}`
            }
        ];
        this.uploader = new FileUploader({
            url: '/api/images/raw',
            itemAlias: 'file',
            authTokenHeader: this.authServer.getToken(),
            headers
        });
        this.uploader.onCompleteItem = (item: any, response: any, status: any) => {
            this.onImageUploadFinished(response);
        };
        if (this._images) {
            this._images.forEach((image: IImage) => {
                this.imageEndpoints.push(this.s3ImageResizePipe.transform(image.endpoint, '250x250'));
            });
        } else {
            this._images = [];
        }
    }

    get images() {
        return this._images;
    }

    @Input('images')
    set images(images: IImage[]) {
        this._images = images;
    }

    onFileSelected() {
        this.uploader.uploadAll();
    }

    onImageRemoved(imageEndpoint: String) {
        this._images.forEach((image: IImage, index: number) => {
            if (this.s3ImageResizePipe.transform(image.endpoint, '250x250') === imageEndpoint) {
                this._images.splice(index, 1);
                this.imageEndpoints.splice(index, 1);
            }
        });
    }

    onImageUploadFinished($event: any) {
        const parsedJson = JSON.parse($event);
        const id = parsedJson.id;
        if (id) {
            const image = new Image(id);
            image.endpoint = parsedJson.endpoint;
            this._images.push(image);
            this.imageEndpoints.push(this.s3ImageResizePipe.transform(image.endpoint, '250x250'));
        }
    }
}
