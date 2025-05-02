package com.rudiridho.catalogapps.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rudiridho.catalogapps.R
import com.rudiridho.catalogapps.databinding.FragmentProductListBinding
import com.rudiridho.catalogapps.presentation.adapter.ProductAdapter
import com.rudiridho.catalogapps.presentation.model.ProductUI
import com.rudiridho.catalogapps.presentation.viewmodel.ProductViewModel
import com.rudiridho.catalogapps.utils.UiSafeState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearchView()
        setupFilterButtons()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            productViewModel.updateProduct(product.copy(isFavorite = !product.isFavorite))
        }
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        productViewModel.products.observe(viewLifecycleOwner) { uiSafeState ->
            when (uiSafeState) {
                is UiSafeState.Success -> {
                    // Submit the list of ProductUI to the adapter
                    productAdapter.submitList(uiSafeState.data)
                    Log.d("edosak", "product ${uiSafeState.data}")
                }
                is UiSafeState.Loading -> {
                    // Handle loading state (e.g., show a progress bar)
                }
                is UiSafeState.Error -> {
                    // Handle error state (e.g., showan error message)
                }
                is UiSafeState.ErrorConnection -> {
                    // Handle error connection state
                }
                is UiSafeState.Empty -> {
                    // Handle empty state
                }
                else -> {
                    // Handle uninitialized state
                }
            }
        }

        productViewModel.favoriteProducts.observe(viewLifecycleOwner) { uiSafeState ->
            when (uiSafeState) {
                is UiSafeState.Success -> {
                    productAdapter.submitList(uiSafeState.data)
                    Log.d("edosak", "fav product ${uiSafeState.data}")
                }
                is UiSafeState.Loading -> {
                    // Handle loading state
                }
                is UiSafeState.Error -> {
                    // Handle error state
                }
                is UiSafeState.ErrorConnection -> {
                    // Handle error connection state
                }
                is UiSafeState.Empty -> {
                    // Handle empty state
                }
                else -> {
                    // Handle uninitialized state
                }
            }
        }

        productViewModel.searchedProducts.observe(viewLifecycleOwner) { uiSafeState ->
            when (uiSafeState) {
                is UiSafeState.Success -> {
                    showProductList(uiSafeState.data)
                }
                is UiSafeState.Loading -> {
                    showLoading()
                }
                is UiSafeState.Error -> {
                    showError(uiSafeState.message)
                }
                is UiSafeState.ErrorConnection -> {
                    showError(getString(R.string.error_connection))
                }
                is UiSafeState.Empty -> {
                    showEmptyState()
                }
                else -> {
                    // Handle uninitialized state
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    productViewModel.searchProducts(it)
                }
                return true
            }
        })
    }

    private fun setupFilterButtons() {
        binding.btnAll.setOnClickListener {
            productViewModel.getProducts()
        }

        binding.btnFavorites.setOnClickListener {
            productViewModel.getFavoriteProducts()
        }
    }

    private fun setupRetryButton() {
        binding.errorStateLayout.retryButton.setOnClickListener {
            productViewModel.getProducts()
        }
    }

    private fun showProductList(products: List<ProductUI>) {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.VISIBLE
        productAdapter.submitList(products)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.errorStateLayout.root.visibility = View.VISIBLE
        binding.rvProducts.visibility = View.GONE
        binding.errorStateLayout.errorTextView.text = message ?: getString(R.string.error_state_message)
    }

    private fun showEmptyState() {
        binding.progressBar.visibility = View.GONE
        binding.emptyStateLayout.root.visibility = View.VISIBLE
        binding.errorStateLayout.root.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}