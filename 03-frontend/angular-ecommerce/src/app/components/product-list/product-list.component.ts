import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartItem } from 'src/app/common/cart-item';
import { Product } from 'src/app/common/product';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list-grid.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentCateegoryId: number = 1;
  currentCategoryName: string = "";
  searchMode: boolean = false;
  previousCategoryId: number = 1;

  // properties for pagination
  thePageNumber: number = 1;
  thePageSize: number = 10;
  theTotalElements: number = 0;

  previousKeyword: string = "";

  constructor(private productService: ProductService, private cartService: CartService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.listOfProducts();
    });
  }


  listOfProducts() {
    
    this.searchMode = this.route.snapshot.paramMap.has('keyword');
    
    if (this.searchMode) {
      this.handleSearchProducts();
    } else {
      this.handleListProducts();
    }

  }
  
  handleSearchProducts() {
    const theKeyword: string = this.route.snapshot.paramMap.get('keyword')!;

    // if we have a different keyword than previous then set page number to 1
    if (this.previousKeyword != theKeyword) {
      this.thePageNumber = 1;
    }

    this.previousKeyword = theKeyword;

    console.log(`keyword=${theKeyword}, thePageNumber=${this.thePageNumber}`);

    // search for the products using keyword
    this.productService.searchProductsPaginate(this.thePageNumber - 1,
                                               this.thePageSize,
                                               theKeyword).subscribe(
                                                  this.processResult()
                                                );
  }
 
  processResult() {
    return (data: any) => {
      this.products = data._embedded.products;
      this.thePageNumber = data.page.number + 1;
      this.thePageSize = data.page.size;
      this.theTotalElements = data.page.totalElements;
    };
  }

  handleListProducts() {
    // check if id parameter id available
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      // get the id as a string. convert string to a number using the "+" symbol
      this.currentCateegoryId = +this.route.snapshot.paramMap.get('id')!;
      // get the "name" param string
      this.currentCategoryName = this.route.snapshot.paramMap.get('name')!;
    } else {
      // not category id available default 1
      this.currentCateegoryId = 1;
      this.currentCategoryName = 'Books';
    }

    // check if we have a different category than previous
    // Angular will reuse a component if it`s currentlu being viewed
    if (this.previousCategoryId != this.currentCateegoryId) {
      this.thePageNumber = 1;
    }

    this.previousCategoryId = this.currentCateegoryId;
    console.log(`currentCategoryId=${this.currentCateegoryId}, thePageNumber=${this.thePageNumber}`);

    // get the products for the given category id
    this.productService.getProductListPaginate(this.thePageNumber-1,
                                               this.thePageSize,
                                               this.currentCateegoryId)
                                               .subscribe(this.processResult());
  }

  updatePageSize(pageSize: string) {
    this.thePageSize = +pageSize;
    this.thePageNumber = 1;
    this.listOfProducts();
  }

  addToCart(theProduct: Product) {

    console.log(`Adding to cart: ${theProduct.name}, ${theProduct.unitPrice}`);

    let theCartItem = new CartItem(theProduct.id, theProduct.name, theProduct.imageUrl, theProduct.unitPrice);
    this.cartService.addToCart(theCartItem);

  }

}
