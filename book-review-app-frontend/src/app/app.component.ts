import {Component} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {Review} from './entity/review';
import {ReviewService} from './service/review.service';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {ServerResponse} from "./entity/ServerResponse";
import {Modal} from 'bootstrap';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    DatePipe,
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatNativeDateModule,
    MatInputModule,
    MatDatepickerModule
  ],
  providers: [DatePipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  bookReviewList: Array<Review> = [];

  bookReviewForm!: FormGroup;
  csearch!: FormGroup;
  private oldreview!: Review;
  private review!: Review;
  enaAdd: boolean = true;
  enaUpdate: boolean = false;

  cscolumns: string[] = ['csrating', 'csdate'];
  csprompts: string[] = ['Search by Rating', 'Search by Date'];

  constructor(private rs: ReviewService, private fb: FormBuilder, private dp: DatePipe) {

    this.csearch = this.fb.group({
      'csrating': new FormControl(),
      'csdate': new FormControl()

    });

    this.bookReviewForm = this.fb.group({
      "bookTitle": new FormControl('', [Validators.required]),
      "author": new FormControl('', [Validators.required]),
      "rating": new FormControl('', [Validators.required, Validators.pattern("^[1-5](?:\\.\\d+)?$")]),
      "review": new FormControl('', [Validators.required]),
      "dateAdded": new FormControl({value: new Date(), disabled: true}, [Validators.required]),
    });
  }

  ngOnInit() {
    this.initialize();
  }

  private initialize() {

    this.rs.getAllReviews().then((rev: Review[]) => {
      this.bookReviewList = rev;
    });

    Object.values(this.bookReviewForm.controls).forEach(control => {
      control.markAsUntouched();
      control.markAsPristine();
    });

    for (const controlName in this.bookReviewForm.controls) {
      const control = this.bookReviewForm.controls[controlName];
      control.valueChanges.subscribe(value => {
          // @ts-ignore
          if (controlName == "dateadded")
            value = this.dp.transform(new Date(value), 'yyyy-MM-dd');

          if (this.oldreview != undefined && control.valid) {
            // @ts-ignore
            if (value === this.client[controlName]) {
              control.markAsPristine();
            } else {
              control.markAsDirty();
            }
          } else {
            control.markAsPristine();
          }
        }
      );

    }
  }

  reload() {
    this.rs.getAllReviews().then((rev: Review[]) => {
      this.bookReviewList = rev;
    });
  }

  add() {
    this.review = this.bookReviewForm.getRawValue();
    console.log(this.review);
    // @ts-ignore
    this.review.dateAdded = this.dp.transform(this.review.dateAdded, 'yyyy-MM-dd');
    console.log(this.review.bookTitle, this.review.dateAdded);
    this.rs.add(this.review).then((response: ServerResponse) => {
      const code = response.code;
      const secondModalElement = document.getElementById('dialog-box');
      this.openDialogBox(response.message, response.code);

    });

  }

  openDialogBox(message: string, status: string) {
    const secondModalElement = document.getElementById('dialog-box');
    const modalBodyElement = document.getElementById('modalBody');

    const modalTitleElement = document.getElementById('modalTitle');

    modalTitleElement!.textContent = 'Review Status';
    modalBodyElement!.textContent = message;


    // Bootstrap modal instance with focus trap disabled
    const secondModal = new Modal(secondModalElement!, {
      focus: false
    });
    if (status === "200") {
      // console.log("awa");
      secondModalElement!.removeEventListener('hidden.bs.modal', this.resetFormAfterModalClose);
      secondModalElement!.addEventListener('hidden.bs.modal', () => {
        this.resetFormAfterModalClose();
      });
    }
    secondModal.show();
  }

  resetFormAfterModalClose() {

    this.bookReviewForm.reset();
    Object.values(this.bookReviewForm.controls).forEach(control => control.markAsUntouched());
    this.reload();
    const closeButton = document.querySelector('[data-bs-dismiss="modal"]');
    if (closeButton) {
      // @ts-ignore
      closeButton.click();

    }
  }

  cancel() {
    this.bookReviewForm.reset();
    Object.values(this.bookReviewForm.controls).forEach(control => control.markAsUntouched());
    this.bookReviewForm.get('dateAdded')?.setValue(new Date());
  }

  id!: number;


  update(br: Review) {
    this.enaUpdate = true;
    this.enaAdd = false;
    const addNewReviewButton = document.querySelector('[data-bs-toggle="modal"][data-bs-target="#newreview"]') as HTMLButtonElement;
    addNewReviewButton.click();

    this.id = br.id;
    this.bookReviewForm.get("bookTitle")?.setValue(br.bookTitle);
    this.bookReviewForm.get('author')?.setValue(br.author);
    this.bookReviewForm.get('rating')?.setValue(br.rating);
    this.bookReviewForm.get('review')?.setValue(br.review);
    this.bookReviewForm.get('dateAdded')?.setValue(br.dateAdded);
  }

  delete(br: Review) {

    this.rs.delete(br.id).then((response: ServerResponse) => {
      const code = response.code;
      const secondModalElement = document.getElementById('dialog-box');
      this.openDialogBox(response.message, response.code);


    });
  }

  updateinner() {

    this.review = this.bookReviewForm.getRawValue();
    // @ts-ignore
    this.review.dateAdded = this.dp.transform(this.review.dateAdded, 'yyyy-MM-dd');
    this.review.id = this.id;

    this.rs.update(this.review).then((response: ServerResponse) => {
      const code = response.code;
      // console.log(response);
      const secondModalElement = document.getElementById('dialog-box');
      this.openDialogBox(response.message, code);

    });

  }

  filterData() {
    const csearchdata = this.csearch.getRawValue();

    if (!csearchdata.csrating && !csearchdata.csdate) {
      this.reload();
      return;
    }
    this.bookReviewList = this.bookReviewList.filter((review) => {
      const matchesRating = !csearchdata.csrating || review.rating.toString().includes(csearchdata.csrating.toString());
      const matchesDate = !csearchdata.csdate || review.dateAdded.includes(csearchdata.csdate);
      return matchesRating && matchesDate;
    });
  }


}

